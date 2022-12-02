file_path = File.expand_path('day_01.txt', __dir__)
input = File.read(file_path)
#input = 'R5, L5, R5, R3'

x, y = 0, 0

direction = :UP

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
  case direction
  when :UP
    y -= steps
  when :LEFT
    x -= steps
  when :DOWN
    y += steps
  when :RIGHT
    x += steps
  end
end

puts x.abs + y.abs

#287
