ultimaCopa = {
    ["ano"] = 2002,
    sede = "Japao e Coreia do Sul",
    jogadores = {"Cafu", "R9", "R10"},
    imprime = function(self)
        for k,v in pairs(self.jogadores) do
            print(k, v)
        end
    end

}

ultimaCopa.capitao = "Cafu"

print(ultimaCopa["ano"])
print(ultimaCopa.ano)

for k,v in pairs(ultimaCopa) do
    print(k, v)
end

table.insert(ultimaCopa.jogadores, "Rivaldo")
table.insert(ultimaCopa.jogadores, "Zico")
table.remove(ultimaCopa.jogadores, 4)

-- ultimaCopa.imprime(ultimaCopa)
ultimaCopa:imprime()