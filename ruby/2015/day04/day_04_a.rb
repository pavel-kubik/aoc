require 'digest'

file_path = File.expand_path('day_04.txt', __dir__)
input = File.read(file_path)
#input = 'pqrstuv'

(1..1_500_000).each do |n|
  md5 = Digest::MD5.hexdigest input + n.to_s
  #puts "#{n} => #{md5}"
  puts n if md5.start_with?('00000')
end

#346386
