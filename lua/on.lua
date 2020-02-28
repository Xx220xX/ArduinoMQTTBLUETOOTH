c = Comando.new(1)
c.descricao = [[Liga a tomada]]
c.name = 'Sempre ligado'
c.code = 'on'
c.args[1] =  Args.new('Tomada','dropDown',[[refere-se a qual tomada será aplicado a configuração]],'a tomada possui somente 4 plugins disponiveis',function (t) return t>=0 and t<= Comando.getParam('plug')  end)
c.args[1].list['Tomada 1']=0
c.args[1].list['Tomada 2']=1
c.args[1].list['Tomada 3']=2
c.args[1].list['Tomada 4']=3
return c

