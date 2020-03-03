c = Comando.new(2)
c.descricao = [[Liga e desliga a tomada por um tempo passado ]]
c.name = 'Pisca Pisca'
c.code = 'blink'
c.args[1] =  Args.new('Tomada','dropDown',[[refere-se a qual tomada será aplicado a configuração]],'a tomada possui somente 4 plugins disponiveis',function (t) return t>=0 and t<= Comando.getParam('plug')  end)
c.args[1].list['Tomada 1']=0
c.args[1].list['Tomada 2']=1
c.args[1].list['Tomada 3']=2
c.args[1].list['Tomada 4']=3

c.args[2] =  Args.new('periodo','float',[[para alternar entre on/off, está em segundos]],'tempo invalido, por segurança deve ser maior que 0.1',function (t) return t>=0.1 end)
c.args[2].getValue = function (val ) return val*1000 end
	
return c

