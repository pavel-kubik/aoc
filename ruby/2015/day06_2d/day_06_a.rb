file_path = File.expand_path('day_06.txt', __dir__)
input = File.read(file_path)
#input = 'turn on 0,0 through 999,999'
#input = 'toggle 0,0 through 999,0'
#input = 'turn off 499,499 through 500,500'

$lights = Array.new(1000) { Array.new(1000, 0) }

def apply(from, to, operation)
  (from[0].to_i..to[0].to_i).each do |x|
    (from[1].to_i..to[1].to_i).each do |y|
      $lights[x][y] = method(operation).call($lights[x][y])
    end
  end
end

def turn_on(light)
  1
end

def turn_off(light)
  0
end

def toggle(light)
  light == 1 ? 0 : 1
end

input.each_line do |line|
  if line.start_with?('turn on')
    dims = line.delete_prefix('turn on').split('through').each { |l| l.strip }
    from = dims[0].split(',')
    to = dims[1].split(',')
    apply(from, to, :turn_on)
  elsif line.start_with?('turn off')
    dims = line.delete_prefix('turn off').split('through').each { |l| l.strip }
    from = dims[0].split(',')
    to = dims[1].split(',')
    apply(from, to, :turn_off)
  elsif line.start_with?('toggle')
    dims = line.delete_prefix('toggle').split('through').each { |l| l.strip }
    from = dims[0].split(',')
    to = dims[1].split(',')
    apply(from, to, :toggle)
  else
    throw "Unknown operation"
  end
end

puts $lights.flatten.inject(0, :+)

# 905970 high
# 458330 low
# 569999
