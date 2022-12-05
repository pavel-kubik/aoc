file_path = File.expand_path('day_05.txt', __dir__)
input = File.read(file_path)

cargo, operations = input.split("\n\n")

stack = Array.new(9) {Array.new}
def add(stack, item)
  if item.to_i.to_s != item && item != ' '
    stack.unshift(item)
  end
end

cargo.each_line.each_with_index do |line, index|
  0..9.times do |i|
    add(stack[i], line[1 + i * 4])
  end
end

operations.each_line do |line|
  line.match(/move (\d+) from (\d+) to (\d+)/) do |m|
    m[1].to_i.times do
      item = stack[m[2].to_i - 1].pop
      stack[m[3].to_i - 1].push(item)
    end
  end
end

puts stack.map(&:pop).join

# BZLVHBWQF
