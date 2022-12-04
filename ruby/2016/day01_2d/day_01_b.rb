require 'set'

file_path = File.expand_path('day_01.txt', __dir__)
input = File.read(file_path)
#input = 'R8, R4, R4, R8'

x, y = 0, 0

direction = :UP

positions = Set.new

def turn_right(direction)
  case direction
  when :UP
    :RIGHT
  when :RIGHT
    :DOWN
  when :DOWN
    :LEFT
  when :LEFT
    :UP
  end
end

def turn_left(direction)
  case direction
  when :UP
    :LEFT
  when :LEFT
    :DOWN
  when :DOWN
    :RIGHT
  when :RIGHT
    :UP
  end
end

input.split(', ').each do |instruction|
  direction = if instruction[0] == 'L'
                turn_left(direction)
              else
                turn_right(direction)
              end
  steps = instruction.slice(1..-1).to_i
  steps.times do
    case direction
    when :UP
      y -= 1
    when :LEFT
      x -= 1
    when :DOWN
      y += 1
    when :RIGHT
      x += 1
    end
    if positions.include?([x, y])
      puts x.abs + y.abs
      return
    end
    positions.add([x, y])
  end
end


