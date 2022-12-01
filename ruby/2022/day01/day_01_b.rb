file_path = File.expand_path('day_01.txt', __dir__)
input = File.read(file_path)

calories = Array.new(1, 0)
elf_index = 0

input.each_line do |line|
  if line.chomp.empty?
    elf_index += 1
    calories.push(0)
  else
    calories[elf_index] += line.to_i
  end
end

puts calories.max(3).sum
