import {AfterViewInit, Component, computed, ElementRef, input, output, viewChildren} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'lib-confirmation-code',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './confirmation-code.component.html',
  styleUrl: './confirmation-code.component.scss'
})
export class ConfirmationCodeComponent implements AfterViewInit {
  codeComplete = output<string>()
  codeChange = output<string>()
  digitsLength = input(5)

  digitIndices = computed(() => Array.from({length: this.digitsLength()}, (_, i) => i))

  codeForm: FormGroup

  codeInputs = viewChildren<ElementRef>('codeInput')


  constructor(private fb: FormBuilder) {
    const formControls: { [key: string]: any } = {}

    for (let i = 0; i < this.digitsLength(); i++) {
      formControls[`digit${i}`] = ['', [Validators.required, Validators.pattern(/^\d$/)]];
    }

    this.codeForm = this.fb.group(formControls)
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.codeInputs()[0]?.nativeElement.focus();
    });
  }

  onInput(event: any, index: number) {
    let value = event.target.value

    if (value.length > 1) {
      // Take the last entered digit (the new one)
      value = value.slice(-1);
      event.target.value = value;
      this.codeForm.get(`digit${index}`)?.setValue(value);
    }

    if (value && index < this.digitsLength() - 1) {
      const nextInput = this.codeInputs()[index + 1]
      nextInput?.nativeElement.focus()
    }

    this.emitCode();
  }


  onKeyDown(event: KeyboardEvent, index: number) {
    const currentInput = event.target as HTMLInputElement;

    // Handle numeric input - replace existing digit
    if (/^\d$/.test(event.key)) {
      currentInput.value = '';
      this.codeForm.get(`digit${index}`)?.setValue('');
      return; // Let the input event handle the new digit
    }

    // Handle backspace
    if (event.key === 'Backspace') {
      const currentInput = event.target as HTMLInputElement;

      if (!currentInput.value && index > 0) {
        // Move to previous input if current is empty
        const prevInput = this.codeInputs()[index - 1];
        prevInput?.nativeElement.focus();
      }
    }

    // Handle arrow keys
    if (event.key === 'ArrowLeft' && index > 0) {
      const prevInput = this.codeInputs()[index - 1];
      prevInput?.nativeElement.focus();
    }

    if (event.key === 'ArrowRight' && index < this.digitsLength() - 1) {
      const nextInput = this.codeInputs()[index + 1];
      nextInput?.nativeElement.focus();
    }

    // Prevent non-numeric input
    if (!/^\d$/.test(event.key) &&
      !['Backspace', 'Delete', 'Tab', 'ArrowLeft', 'ArrowRight'].includes(event.key)) {
      event.preventDefault();
    }
  }

  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedData = event.clipboardData?.getData('text') || '';
    const digits = pastedData.replace(/\D/g, '').slice(0, this.digitsLength());

    if (digits.length > 0) {
      const inputArray = this.codeInputs();

      for (let i = 0; i < this.digitsLength(); i++) {
        const digit = digits[i] || '';
        this.codeForm.get(`digit${i}`)?.setValue(digit);
        inputArray[i].nativeElement.value = digit;
      }

      // Focus on the next empty field or last field
      const focusIndex = Math.min(digits.length, this.digitsLength() - 1);
      inputArray[focusIndex]?.nativeElement.focus();

      this.emitCode();
    }
  }

  private emitCode() {
    const code = Array.from({length: this.digitsLength()}, (_, i) =>
      this.codeForm.get(`digit${i}`)?.value || ''
    ).join('');

    this.codeChange.emit(code);

    if (code.length === this.digitsLength() && this.codeForm.valid) {
      this.codeComplete.emit(code);
    }
  }


  getCode(): string {
    return Array.from({length: this.digitsLength()}, (_, i) =>
      this.codeForm.get(`digit${i}`)?.value || ''
    ).join('');
  }

  clearCode() {
    this.codeForm.reset();
    this.codeInputs()[0].nativeElement.focus();
  }

  get isComplete(): boolean {
    return this.codeForm.valid && this.getCode().length === this.digitsLength();
  }

  isFieldInvalid(index: number): boolean {
    const field = this.codeForm.get(`digit${index}`);
    return !!(field && field.invalid && field.touched);
  }
}
