file_path_test = File.expand_path('day_07_test.txt', __dir__)
input_test = File.read(file_path_test)

file_path = File.expand_path('day_07.txt', __dir__)
input = File.read(file_path)

$wire = Hash.new
$cache = Hash.new

class Node
  attr_accessor :to, :in1, :in2, :value, :op

  def initialize(to)
    @to = to
  end
end

input.each_line do |line|
  from, to = line.chomp.split(' -> ')
  puts "#{from} -> #{to}"
  node = Node.new(to)
  $wire[to] = node
  case from
  when /AND/
    puts 'found AND'
    node.op = :and
    node.in1, node.in2 = from.split(' AND ')
  when /OR/
    puts 'found OR'
    node.op = :or
    node.in1, node.in2 = from.split(' OR ')
  when /LSHIFT/
    puts 'found LSHIFT'
    node.op = :lshift
    node.in1, node.in2 = from.split(' LSHIFT ')
  when /RSHIFT/
    puts 'found RSHIFT'
    node.op = :rshift
    node.in1, node.in2 = from.split(' RSHIFT ')
  when /NOT/
    puts 'found NOT'
    node.op = :not
    node.in1 = from.split('NOT ')[1]
  when /(\d+)/
    puts "found number #{Regexp.last_match(1)}"
    node.op = :number
    node.value = Regexp.last_match(1)
  when /(\w+)/
    puts "found wire #{Regexp.last_match(1)}"
    node.op = :wire
    node.in1 = Regexp.last_match(1)
  else
    throw "unknown op #{from}"
  end
end

$wire['b'].op = :number
$wire['b'].value = 46_065

class String
  def is_integer?
    self.to_i.to_s == self
  end
end

def eval_node(node)
  if node.is_a?(String) && node.start_with?(/\d/)
    return node.to_i
  end
  unless node.is_a?(Node)
    if $wire[node].nil?
      throw "unknown node #{node}"
    end
    node = $wire[node]
  end
  if $cache.key?(node.to)
    return $cache[node.to]
  end
  value = case node.op
          when :number
            node.value.to_i
          when :and
            eval_node(node.in1) & eval_node(node.in2)
          when :or
            eval_node(node.in1) | eval_node(node.in2)
          when :lshift
            eval_node(node.in1) << eval_node(node.in2)
          when :rshift
            eval_node(node.in1) >> eval_node(node.in2)
          when :not
            ~eval_node(node.in1).to_i % 0xFFFF + 1  # why + 1? 0xFFFF is 65535, not 65536
          when :wire
            eval_node(node.in1)
          end
  if node.is_a?(Node)
    $cache[node.to] = value
  end
  value
end


# $wire.each do |key, node|
#   puts "node: #{key}: #{eval_node(node)}"
# end

puts "a: #{eval_node('a')}"
