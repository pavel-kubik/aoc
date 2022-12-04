file_path = File.expand_path('day_05.txt', __dir__)
input = File.read(file_path)

$vowels = 'aeiou'
$forbidden = %w[ab cd pq xy]

def double(word)
  last = ''
  word.chars do |char|
    return true if last == char
    last = char
  end
  false
end

def nice(word)
  vowelsCount = word.chars.inject(0) { |sum, char| sum += $vowels.include?(char) ? 1 : 0 }
  return false unless vowelsCount >= 3

  return false unless double(word)

  $forbidden.each do |forbidden|
    return false if word.include?(forbidden)
  end

  true
end

puts input.each_line.inject(0) { |sum, line| sum += nice(line) ? 1 : 0 }

#TODO
# try shortcut for boolean https://stackoverflow.com/questions/13537206/how-do-i-convert-boolean-values-to-integers
