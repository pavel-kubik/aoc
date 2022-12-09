require 'set'
file_path = File.expand_path('day_09.txt', __dir__)
input = File.read(file_path)

head_x, head_y = 0, 0
tail_x, tail_y = 0, 0

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
  return tail_x, tail_y
end

seen = Set.new
input.each_line do |line|
  steps = line[1..].to_i
  puts "steps: #{steps} #{line[0]}"
  steps.times do
    case line[0]
    when 'R'
      head_x += 1
    when 'L'
      head_x -= 1
    when 'U'
      head_y += 1
    when 'D'
      head_y -= 1
    end
    # TODO why assignment return values into two values works here and
    #   not work with array in part 2?!
    tail_x, tail_y = align_tail(head_x, head_y, tail_x, tail_y)
    puts "head: #{head_x}, #{head_y} tail: #{tail_x}, #{tail_y}"
    seen << [tail_x, tail_y]
  end
end

puts seen.size

# 6190
