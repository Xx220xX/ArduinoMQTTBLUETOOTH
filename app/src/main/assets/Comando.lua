_SMT_CODES = { plug = 4 }
Comando = {}
Comando.__index = Comando
function Comando.getParam(code)
    return _SMT_CODES[code]
end
function Comando.new(n_args)
    return setmetatable({ name = '', code = '', descricao = '', n_args = n_args, args = {} }, Comando)
end


