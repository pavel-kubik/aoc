file_path = File.expand_path('day_02.txt', __dir__)
input = File.read(file_path)
#input = '2x3x4'
#input = '1x1x10'

def ribon(box)
  dim = box.split('x').map(&:to_i)
  areas = dim.min(2)
  2*areas.sum + dim.inject(&:*)
end

puts input.each_line.inject(0) { |sum, box| sum + ribon(box) }