-- load 桶

-- @author YuJie
-- @modifier lzl
local len = #KEYS - 1
local setKey = KEYS[1]

for i = 1, len do
    print(len)
    local key = KEYS[i + 1]
    local quantity = tonumber(ARGV[i])
    if (redis.call('exists', key) == 0) then
        redis.call('sadd', setKey, key)
        redis.call('set', key, quantity)
    end
end
