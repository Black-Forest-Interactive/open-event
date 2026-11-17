import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms'

export function atLeastOneRequiredValidator(controlNames: string[]): ValidatorFn {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    const isValid = controlNames.some((controlName) => {
      const control = formGroup.get(controlName)
      return control && control.value
    })

    // Mark all controls as touched and invalid if none are set
    controlNames.forEach((controlName) => {
      const control = formGroup.get(controlName)
      if (control) {
        control.setErrors(isValid ? null : { required: true })
      }
    })

    return isValid ? null : { atLeastOneRequired: true }
  }
}
