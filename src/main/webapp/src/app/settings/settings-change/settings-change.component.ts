import {Component, Input} from '@angular/core';
import {Setting, SettingChangeRequest} from "../model/settings-api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SettingService} from "../model/setting.service";
import {HotToastService} from "@ngneat/hot-toast";
import {TranslateService} from "@ngx-translate/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";

@Component({
  selector: 'app-settings-change',
  templateUrl: './settings-change.component.html',
  styleUrls: ['./settings-change.component.scss']
})
export class SettingsChangeComponent {

  title: string = "SETTING.CHANGE.Create";
  reloading: boolean = false

  @Input() data: Setting | null = null


  form: FormGroup = this.fb.group({
      key: ['', Validators.required],
      value: ['', Validators.required],
      type: ['', Validators.required]
    }
  )

  constructor(
    private fb: FormBuilder,
    private location: Location,
    private service: SettingService,
    private toastService: HotToastService,
    private translationService: TranslateService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadData(+id, (e) => this.handleDataEdit(e))
    } else if (this.data) {
      this.handleDataEdit(this.data)
    } else {
      this.handleDataCreate()
    }
  }

  private loadData(settingId: number, callback: (e: Setting) => void) {
    this.reloading = true;
    this.service.getSetting(settingId).subscribe(data => callback(data));
  }

  private handleDataCreate() {
    this.data = null;
    this.translationService.get("SETTING.CHANGE.Create").subscribe(text => this.title = text);
  }

  private handleDataEdit(data: Setting) {
    this.data = data;
    this.initValues(data);
    this.translationService.get("SETTING.CHANGE.Update", {setting: data.id}).subscribe(text => this.title = text);
    this.validateForm()
    this.form.controls['key'].disable()
    this.form.controls['type'].disable()
    this.reloading = false;
  }

  private initValues(data: Setting) {
    this.form.get('key')?.setValue(data.key)
    this.form.get('value')?.setValue(data.value)
    this.form.get('type')?.setValue(data.type)
  }


  private validateForm() {
    this.form.markAllAsTouched()
  }

  onSubmit() {
    if (this.form.invalid) {
      this.validateForm()
      this.showFormInvalidError()
      return
    }

    this.reloading = true;
    if (this.data == null) {
      this.create()
    } else {
      this.update(this.data)
    }
  }

  cancel() {
    this.location.back()
  }

  private get request(): SettingChangeRequest | null {
    this.form.controls['key'].enable()
    this.form.controls['type'].enable()
    return this.form.value
  }

  private create() {
    let request = this.request
    if (!request) {
      this.showFormInvalidError()
    } else {
      this.service.createSetting(request).subscribe((result) => this.handleCreateResult(result))
    }
  }

  private showFormInvalidError() {
    this.translationService.get("SETTING.Message.FormInvalid").subscribe(
      msg => this.toastService.error(msg)
    )
  }

  private update(setting: Setting) {
    let request = this.request
    if (!request) {
      this.showFormInvalidError()
    } else {
      this.service.updateSetting(setting.id, request).subscribe((result) => this.handleCreateResult(result))
    }
  }

  private handleCreateResult(result: Setting) {
    if (result == null) {
      this.translationService.get("SETTING.Message.CreateFailure").subscribe(
        msg => this.toastService.error(msg)
      )
    } else {
      this.translationService.get("SETTING.Message.CreateSuccess").subscribe(
        msg => {
          this.toastService.success(msg)
          this.router.navigate(["/backoffice/settings"]).then()
        }
      )
    }
  }
}
