require 'set'
file_path = File.expand_path('day_11.txt', __dir__)
input = File.read(file_path)

class Monkey
  attr_reader :items, :worry_op, :test, :true_case, :false_case, :inspected

  def initialize(items, worry_op, test, true_case, false_case)
    @items = items
    @worry_op = worry_op
    @test = test
    @true_case = true_case
    @false_case = false_case
    @inspected = 0
  end

  def step
    return if @items.empty?

    @items.size.times do |i|
      # pick item
      ii = @items.shift
      # inspect
      @inspected += 1
      # operation TODO move parsing
      worry_operation = @worry_op.split(' old ')[1].split(' ')[0]
      worry_operand = @worry_op.split(' old ')[1].split(' ')[1]
      ii = if worry_operation == '+'
             worry_operand == 'old' ? ii + ii : ii + worry_operand.to_i
           else
             worry_operand == 'old' ? ii * ii : ii * worry_operand.to_i
           end
      # calm down
      ii %= 2*3*5*7*11*13*17*19
      # test
      if ii % @test == 0
        $monkeys[@true_case].catch(ii)
      else
        $monkeys[@false_case].catch(ii)
      end
    end
  end

  def catch(item)
    @items << item
  end

  def to_s
    "Monkey: #{@inspected}"
  end
end

$monkeys = []

input.split("\n\n").map do |group|
  lines = group.split("\n")
  monkey_idx = lines[0].split(' ')[1][0].to_i
  puts "Monkey #{monkey_idx}"
  items = lines[1].split('Starting items: ')[1].split(', ').map(&:to_i)
  puts "Items: #{items}"
  worry_op = lines[2].split('Operation: ')[1]
  puts "Worry operation: #{worry_op}"
  test = lines[3].split('Test: ')[1].split(' ')[2].to_i
  puts "Test: #{test}"
  true_case = lines[4].split('If true: throw to monkey ')[1].to_i
  false_case = lines[5].split('If false: throw to monkey ')[1].to_i
  puts "True case: #{true_case}"
  puts "False case: #{false_case}"
  $monkeys << Monkey.new(items, worry_op, test, true_case, false_case)
end

100.times do |i|
  100.times do
    $monkeys.each(&:step)
  end
  puts $monkeys.each(&:to_s)
  puts "= #{i} ==================="
end

puts $monkeys.map(&:inspected).max(2).inject(:*)