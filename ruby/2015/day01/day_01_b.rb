file_path = File.expand_path('day_01.txt', __dir__)
input = File.read(file_path)

# this code not work for example string - condition in break is not processed for last item
puts input.chars.each_with_index.inject(0) { |floor, (char, idx)|
  break idx if floor == -1
  next floor += 1 if char == '('
  next floor -= 1 if char == ')'
}

# 1783