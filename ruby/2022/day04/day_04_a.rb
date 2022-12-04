file_path = File.expand_path('day_04.txt', __dir__)
input = File.read(file_path)

sum = 0

def full_overlap(first, second)
  a, b = first.split('-').map(&:to_i)
  c, d = second.split('-').map(&:to_i)
  (a >= c && b <= d) || (c >= a && d <= b)
end

input.each_line do |line|
  first, second = line.split(',')
  sum += 1 if full_overlap(first, second)
end

puts sum

# 556
