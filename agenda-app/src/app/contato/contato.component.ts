import { Component, OnInit } from '@angular/core';
import { ContatoService } from '../contato.service';
import { Contato } from './contato';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-contato',
  templateUrl: './contato.component.html',
  styleUrls: ['./contato.component.css']
})
export class ContatoComponent implements OnInit {

  formulario: FormGroup = this.fb.group({
    nome: ['', Validators.required],
    email: ['', Validators.email]
  });

  constructor(private service: ContatoService, private fb : FormBuilder) { }

  ngOnInit(): void {
    this.formulario = this.fb.group({
      nome: ['', Validators.required],
      email: ['', Validators.email]
    })
  }

  submit(){
    console.log(this.formulario.value)
    /*this.service.save(c).subscribe( resposta => {
      console.log(resposta);
    })*/
  }

}
