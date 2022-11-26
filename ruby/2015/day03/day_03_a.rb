require 'set'

file_path = File.expand_path('day_03.txt', __dir__)
input = File.read(file_path)
#input = '>'
#input = '^v^v^v^v^v'

houses = Set.new

x, y = 0, 0

houses.add([x, y])

input.each_char do |c|
  case c
  when '^'
    y -= 1
  when 'v'
    y += 1
  when '<'
    x -= 1
  when '>'
    x += 1
  else
    throw 'Unknown character ' + c
  end
  houses.add([x, y])
end

puts houses.size

#2081