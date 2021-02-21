copas = { 1958, 1962, 1970, nil, 1994, 2002 }

-- print(copas[1])

print('#copas')

for i = 1,#copas do
    print(i, copas[i])
end

print('ipairs')

for i,v in ipairs(copas) do
    print(i, v)
end

print('pairs')

for i,v in pairs(copas) do
    print(i, v)
end
