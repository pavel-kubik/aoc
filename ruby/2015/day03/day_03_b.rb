require 'set'

file_path = File.expand_path('day_03.txt', __dir__)
input = File.read(file_path)
#input = '>'
#input = '^v^v^v^v^v'

class Santa
  attr_reader :houses

  def initialize
    @houses = Set.new
    @x, @y = 0, 0
    @houses.add([@x, @y])
  end

  def step(c)
    case c
    when '^'
      @y -= 1
    when 'v'
      @y += 1
    when '<'
      @x -= 1
    when '>'
      @x += 1
    else
      throw "Unknown character #{c}"
    end
    @houses.add([@x, @y])
  end
end

santa = Santa.new
roboS = Santa.new

input.chars.select.each_with_index { |_, i| i.odd? }.each do |c|
  santa.step(c)
end
input.chars.select.each_with_index { |_, i| i.even? }.each do |c|
  roboS.step(c)
end

puts santa.houses.merge(roboS.houses).size

#2341
