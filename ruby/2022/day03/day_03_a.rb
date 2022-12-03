# frozen_string_literal: true

require 'set'

file_path = File.expand_path('day_03.txt', __dir__)
input = File.read(file_path)

def find(first, second)
  first.each_char.each do |c|
    return c if second.index(c)
  end
end

sum = 0

input.each_line do |line|
  first = line[0..line.length / 2 - 1]
  second = line[line.length / 2..]
  char = find(first, second)
  sum += if /[[:upper:]]/.match(char)
           char.ord - 'A'.ord + 27
         else
           char.ord - 'a'.ord + 1
         end
end

puts sum
