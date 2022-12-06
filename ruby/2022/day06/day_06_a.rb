file_path = File.expand_path('day_06.txt', __dir__)
input = File.read(file_path)

input.each_line do |line|
  0..line.length.times do |i|
    if line.chars[i..i + 3].uniq.length == 4
      puts i + 4
      break
    end
  end
end

# 1480
