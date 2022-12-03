require 'set'

file_path = File.expand_path('day_03.txt', __dir__)
input = File.read(file_path)

def find(first, second, third)
  first.each_char.each do |c|
    return c if second.index(c) && third.index(c)
  end
end

sum = 0

input.lines.each_slice(3) do |a, b, c|
  char = find(a, b, c)
  sum += if /[[:upper:]]/.match(char)
           char.ord - 'A'.ord + 27
         else
           char.ord - 'a'.ord + 1
         end
end

puts sum
