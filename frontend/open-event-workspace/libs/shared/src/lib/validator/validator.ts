import {ValidatorFn} from "@angular/forms";

export function atLeastOneRequiredValidator(controlNames: string[]): ValidatorFn {
  // @ts-ignore
  return (formGroup: FormGroup): ValidationErrors | null => {
    const isValid = controlNames.some(controlName => {
      const control = formGroup.get(controlName);
      return control && control.value;
    });

    // Mark all controls as touched and invalid if none are set
    controlNames.forEach(controlName => {
      const control = formGroup.get(controlName);
      if (control) {
        control.setErrors(isValid ? null : {required: true});
      }
    });

    return isValid ? null : {atLeastOneRequired: true};
  };
}
