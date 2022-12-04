require 'digest'

file_path = File.expand_path('day_04.txt', __dir__)
input = File.read(file_path)
#input = 'pqrstuv'

(5_000_000..10_000_000).each do |n|
  md5 = Digest::MD5.hexdigest input + n.to_s
  #puts "#{n} => #{md5}"
  if md5.start_with?('000000')
    puts n
    break
  end
end

#9958218
