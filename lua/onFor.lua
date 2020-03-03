c = Comando.new(2)
c.descricao = [[Liga a tomada por um tempo depois desliga]]
c.name = 'Ligar por um periodo'
c.code = 'on for'
c.args[1] =  Args.new('Tomada','dropDown',[[refere-se a qual tomada será aplicado a configuração]],'a tomada possui somente 4 plugins disponiveis',function (t) return t>=0 and t<= Comando.getParam('plug')  end)
c.args[1].list['Tomada 1']=0
c.args[1].list['Tomada 2']=1
c.args[1].list['Tomada 3']=2
c.args[1].list['Tomada 4']=3

c.args[2] =  Args.new('Periodo','float',[[refere-se ao tempo  em segundos que permanecera ligada]],'tempo invalido',function (t) return t>=0 end)
c.args[2].getValue = function ( val )return val/1000 end
return c

