file_path = File.expand_path('day_01.txt', __dir__)
input = File.read(file_path)

puts input.split('').reduce(0) { |floor, num|
  floor + (num == '(' ? 1 : -1)
}

# 232
