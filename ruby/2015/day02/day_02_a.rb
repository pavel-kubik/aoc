file_path = File.expand_path('day_02.txt', __dir__)
input = File.read(file_path)
#input = '2x3x4'
#input = '1x1x10'

def area(box)
  dim = box.split('x').map(&:to_i)
  areas = [2*dim[0]*dim[1], 2*dim[0]*dim[2], 2*dim[1]*dim[2]]
  areas.sum + areas.min/2
end

puts input.each_line.inject(0) { |sum, box| sum + area(box) }
