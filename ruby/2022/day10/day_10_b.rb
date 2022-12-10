require 'set'
file_path = File.expand_path('day_10.txt', __dir__)
input = File.read(file_path)

x = 1
cycle = 0

points = [20, 60, 100, 140, 180, 220]
point_idx = 0
sum = 0

crt = Array.new(6) { Array.new(40) { ' '} }

def print_crt(crt)
  crt.each do |row|
    puts row.join
  end
end

input.each_line.each_with_index do |line, i|
  # puts "line #{i}: #{line}"
  if line.start_with?('noop')
    if cycle % 40 >= x - 1 && cycle % 40 <= x + 1
      crt[cycle / 40][cycle % 40] = '#'
    end
    cycle += 1
  else
    if cycle % 40 >= x - 1 && cycle % 40 <= x + 1
      crt[cycle / 40][cycle % 40] = '#'
    end
    cycle += 1
    if cycle % 40 >= x - 1 && cycle % 40 <= x + 1
      crt[cycle / 40][cycle % 40] = '#'
    end
    cycle += 1
  end
  if point_idx < points.size && cycle >= points[point_idx]
    puts "cycle #{cycle} - #{x}"
    sum += points[point_idx] * x
    point_idx += 1
  end
  if line.start_with?('addx')
    x += line[4..].to_i
  end
end

print_crt(crt)

puts "sum: #{sum}"

# 15180

#EALGUPS
#EALGULPS

=begin
####..##..#.....##..#..#.#....###...##..
#....#..#.#....#..#.#..#.#....#..#.#..#.
###..#..#.#....#....#..#.#....#..#.#....
#....####.#....#.##.#..#.#....###..#.##.
#....#..#.#....#..#.#..#.#....#....#..#.
####.#..#.####..###..##..####.#.....###.


####  ##  #     ##  #  # #    ###   ##
#    #  # #    #  # #  # #    #  # #  #
###  #  # #    #    #  # #    #  # #
#    #### #    # ## #  # #    ###  # ##
#    #  # #    #  # #  # #    #    #  #
#### #  # ####  ###  ##  #### #     ###
EALGULPG
=end