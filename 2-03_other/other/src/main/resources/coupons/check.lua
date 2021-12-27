local key = KEYS[1]

local result = redis.call("EXISTS", key)
return result
