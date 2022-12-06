file_path = File.expand_path('day_06.txt', __dir__)
input = File.read(file_path)

input.each_line do |line|
  0..line.length.times do |i|
    if line.chars[i..i + 13].uniq.length == 14
      puts i + 14
      break
    end
  end
end

# 2746
