c = Comando.new(3)
c.descricao = [[Liga a tomada ate que a humidade atinja o maximo]]
c.name = 'Controle de Humidade'
c.code = 'humidade entre'
c.args[1] =  Args.new('Tomada','dropDown',[[refere-se a qual tomada será aplicado a configuração]],'a tomada possui somente 4 plugins disponiveis',function (t) return t>=0 and t<= Comando.getParam('plug')  end)
c.args[1].list['Tomada 1']=0
c.args[1].list['Tomada 2']=1
c.args[1].list['Tomada 3']=2
c.args[1].list['Tomada 4']=3

c.args[2] =  Args.new('Humidade minima','float',[[a tomada ligara abaixo desta]],'recomenda-se humidade minima acima de 20%',function (t) return t>=20  end)
c.args[3] =  Args.new('Humidade maxima','int',[[a tomada desligara acima desta]],'o sensor se limita a 80%',function (t) return t<=80 and t>20 end)

return c

