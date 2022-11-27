file_path = File.expand_path('day_05.txt', __dir__)
input = File.read(file_path)

$vowels = 'aeiou'
$forbidden = %w[ab cd pq xy]

def pair(word)
  word.chars.each_with_index do |char, index|
    next unless word[index + 1]
    return true if word.slice(index + 2..-1)&.include?(char + word[index + 1])
  end
  false
end

def couple(word)
  word.chars.each_with_index do |char, index|
    next unless word[index + 2]
    return true if char == word[index + 2]
  end
  false
end

def nice(word)
  return false unless pair(word)
  return false unless couple(word)

  true
end

#puts nice('ieodomkazucvgmuy')
puts input.each_line.inject(0) { |sum, line| sum += nice(line) ? 1 : 0 }
