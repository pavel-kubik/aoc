file_path = File.expand_path('day_02.txt', __dir__)
input = File.read(file_path)
#input = 'A Y
#B X
#C Z'

def game(player1, player2)
  # player1 rock
  return 0 if player1 == 'X' && player2 == 'A'
  return -1 if player1 == 'X' && player2 == 'B'
  return 1 if player1 == 'X' && player2 == 'C'
  # player 1 paper
  return 1 if player1 == 'Y' && player2 == 'A'
  return 0 if player1 == 'Y' && player2 == 'B'
  return -1 if player1 == 'Y' && player2 == 'C'
  # player 1 scissors
  return -1 if player1 == 'Z' && player2 == 'A'
  return 1 if player1 == 'Z' && player2 == 'B'
  return 0 if player1 == 'Z' && player2 == 'C'
end

def score_choice(choice)
  return 1 if choice == 'X'
  return 2 if choice == 'Y'
  return 3 if choice == 'Z'
end

def score_result(result)
  return 0 if result == -1
  return 3 if result.zero?
  return 6 if result == 1
end

def suggest(player2, strategy)
  # X.. lose
  return 'Z' if player2 == 'A' && strategy == 'X'
  return 'X' if player2 == 'B' && strategy == 'X'
  return 'Y' if player2 == 'C' && strategy == 'X'
  # Y .. draw
  return 'X' if player2 == 'A' && strategy == 'Y'
  return 'Y' if player2 == 'B' && strategy == 'Y'
  return 'Z' if player2 == 'C' && strategy == 'Y'
  # Z .. win
  return 'Y' if player2 == 'A' && strategy == 'Z'
  return 'Z' if player2 == 'B' && strategy == 'Z'
  return 'X' if player2 == 'C' && strategy == 'Z'
end

puts input.each_line.reduce(0) { |sum, line|
  game_rec = line.split(' ')
  #puts "Strategy " + game_rec.to_s
  move = suggest(game_rec[0], game_rec[1])
  #puts "Game " + move.to_s + ", " + game_rec[0]
  result = game(move, game_rec[0])
  #puts "Result " + result.to_s
  score = score_choice(move) + score_result(result)
  #puts "Score: " + score.to_s
  sum + score
}
