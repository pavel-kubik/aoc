require 'set'

file_path = File.expand_path('day_07.txt', __dir__)
input = File.read(file_path)

class FS
  attr_accessor :name, :children, :parent, :size, :is_dir

  def initialize(name)
    @name = name
    @children = []
  end

  def add_child(child)
    @children << child
  end

  def to_s(indent = 0)
    indent_str = '  ' * indent
    out = indent_str + @name + " (dir)\n" if @is_dir
    out = indent_str + @name + " (file, size=" + @size.to_s + ")\n" unless @is_dir
    out += @children.map { |ch| ch.to_s(indent+2) }.join("\n") unless @children.empty?
    return out
  end
  
  def count_size
    @size = @children.map(&:count_size).sum if @is_dir
    return @size.to_i
  end
end

fs = FS.new('/')
fs.is_dir = true
fs.parent = fs  # root node
dirs = Set.new
dirs << fs
cd = fs

input.split('$ ').each do |command|
  next if command.empty?

  if command.lines[0].start_with?('cd')
    # cd
    dir = command.lines[0].split(' ')[1]
    if dir == '..'
      cd = cd.parent
    elsif dir == '/'
      cd = fs
    else
      new_dir = cd.children.find { |child| child.name == dir }
      if new_dir.nil?
        new_dir = FS.new(dir)
        new_dir.parent = cd
        new_dir.is_dir = true
        cd.add_child(new_dir)
        dirs << new_dir
      else
        cd = new_dir
      end
    end
  elsif command.lines()[0].start_with?('ls')
    # ls
    command.lines()[1..-1].each do |line|
      size = line.split(' ')[0]
      name = line.split(' ')[1]
      if size == 'dir'
        # directory
        new_dir = FS.new(name)
        new_dir.parent = cd
        new_dir.is_dir = true
        cd.add_child(new_dir)
        dirs << new_dir
      else
        # file
        new_file = FS.new(name)
        new_file.size = size
        new_file.parent = cd
        new_file.is_dir = false
        cd.add_child(new_file)
        dirs << new_file
      end
    end
  end

  # puts "command: #{command}"
end

puts fs.to_s

fs.count_size

puts "total size: #{fs.size}"
sum = 0
dirs.each do |node|
  next unless node.is_dir
  if node.size.to_i < 100_000
    puts("#{node.name} (#{node.size})")
    sum += node.size.to_i
  end
end

puts "sum: #{sum}"

# 1644735
