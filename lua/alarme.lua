c = Comando.new(6)
c.descricao = [[Liga a tomada por um tempo nos dias passados]]
c.name = 'Alarme'
c.code = 'on for'
c.args[1] =  Args.new('Tomada','dropDown',[[refere-se a qual tomada será aplicado a configuração]],'a tomada possui somente 4 plugins disponiveis',function (t) return t>=0 and t<= Comando.getParam('plug')  end)
c.args[1].list['Tomada 1']=0
c.args[1].list['Tomada 2']=1
c.args[1].list['Tomada 3']=2
c.args[1].list['Tomada 4']=3

c.args[2] =  Args.new('Hora','int',[[]],'tempo invalido',function (t) return t>=0 and t<24 end)
c.args[3] =  Args.new('Minuto','int',[[]],'tempo invalido',function (t) return t>=0 and t<59 end)
c.args[4] =  Args.new('Segundo','int',[[]],'tempo invalido',function (t) return t>=0 and t<59 end)
c.args[5] =  Args.new('Dias da semana','dropDown',[[Dias da semana que para repetir alarme]])
c.args[5].list ={Domingo=1,Segunda=2,Terça=3,Quarta=4,Quinta=5,Sexta=6,Sábado=7}

c.args[6] =  Args.new('Periodo','int',[[refere-se ao tempo em milissegundos que permanecera ligada]],'tempo invalido',function (t) return t>=0 end)

return c

