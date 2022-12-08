file_path = File.expand_path('day_08.txt', __dir__)
input = File.read(file_path)

forest = []
input.each_line do |line|
  forest << line.chomp.split('').map(&:to_i)
end

def eval_tree_top(forest, i, j, t)
  (i - 1).downto(0).each do |x|
    if forest[x][j] >= t
      return false
    end
  end
  puts "{#{i}, #{j}}: ok top"
  return true
end

def eval_tree_left(forest, i, j, t)
  (j - 1).downto(0).each do |y|
    if forest[i][y] >= t
      return false
    end
  end
  puts "{#{i}, #{j}}: ok left"
  return true
end

def eval_tree_down(forest, i, j, t)
  (i + 1..forest.size - 1).each do |x|
    if forest[x][j] >= t
      return false
    end
  end
  puts "{#{i}, #{j}}: ok down"
  return true
end

def eval_tree_right(forest, i, j, t)
  (j + 1..forest[0].size - 1).each do |y|
    if forest[i][y] >= t
      return false
    end
  end
  puts "{#{i}, #{j}}: ok right"
  return true
end

def eval_tree(forest, i, j, t)
  eval_tree_left(forest, i, j, t) || eval_tree_top(forest, i, j, t) || eval_tree_right(forest, i, j, t) || eval_tree_down(forest, i, j, t) ? 1 : 0
end

sum = 0
forest.each_with_index do |row, i|
  if i == 0 || i == forest.size - 1
    sum += row.size
    next
  end
  row.each_with_index do |t, j|
    if j == 0 || j == row.size - 1
      sum += 1
      next
    end
    sum += eval_tree(forest, i, j, t)
  end
end
puts sum
