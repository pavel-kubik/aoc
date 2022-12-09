require 'set'
file_path = File.expand_path('day_09.txt', __dir__)
input = File.read(file_path)

knots_x = Array.new 10, 0
knots_y = Array.new 10, 0

def align_tail(head_x, head_y, tail_x, tail_y)
  if (head_x - tail_x).abs <= 1 && (head_y - tail_y).abs <= 1
    return tail_x, tail_y
  end
  if head_x == tail_x
    if head_y > tail_y
      tail_y += 1
    else
      tail_y -= 1
    end
  elsif head_y == tail_y
    if head_x > tail_x
      tail_x += 1
    else
      tail_x -= 1
    end
  else
    if (head_x - tail_x) > 0
      tail_x += 1
    else
      tail_x -= 1
    end
    if (head_y - tail_y) > 0
      tail_y += 1
    else
      tail_y -= 1
    end
  end
  return [tail_x, tail_y]
end

seen = Set.new
input.each_line do |line|
  steps = line[1..].to_i
  puts "steps: #{steps} #{line[0]}"
  steps.times do
    puts "Step"
    case line[0]
    when 'R'
      knots_x[0] += 1
    when 'L'
      knots_x[0] -= 1
    when 'U'
      knots_y[0] += 1
    when 'D'
      knots_y[0] -= 1
    end
    9.times do |i|
      out = align_tail(knots_x[i], knots_y[i], knots_x[i + 1], knots_y[i + 1])
      knots_x[i + 1] = out[0]
      knots_y[i + 1] = out[1]
      puts "head: #{knots_x[i]}, #{knots_y[i]} tail: #{knots_x[i + 1]}, #{knots_y[i + 1]}"
    end

    seen << [knots_x[9], knots_y[9]]
  end
end

puts seen.size

# 6190 -> 6181 not
# not 1
# 2516
