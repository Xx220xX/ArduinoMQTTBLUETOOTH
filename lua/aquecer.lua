c = Comando.new(2)
c.descricao = [[é ligado caso a temperatura fique menor que o valor especificado. ]]
c.name = 'Aquecer'
c.code = 'aquecer'
c.args[1] =  Args.new('Tomada','dropDown','referente a qual será aplicado a configuração','a tomada possui somente 4 plugins disponiveis',function (t) return t>=0 and t<= Comando.getParam('plug')  end)
c.args[1].list['Tomada 1']=0
c.args[1].list['Tomada 2']=1
c.args[1].list['Tomada 3']=2
c.args[1].list['Tomada 4']=3

c.args[2] =  Args.new('Temperatura','float',' aceitavel','O sensor é limitado a temperaturas entre 10 a 80 graus celsius',function (t) return  t<80 and t>10  end)

return c

