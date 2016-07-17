module Root where

adjacent :: [Integer]
adjacent = []

localMass :: Integer
localMass = 0

aggregate_mass :: Integer
aggregate_mass = 0

sent_mass :: [Integer]
sent_mass = []

received_mass :: [Integer]
received_mass = []

levels :: [Integer]
levels = []

broadcast :: Bool
broadcast = False

new_node :: [Integer] -> [Integer]
new_node x = adjacent ++ x

-- helper functions
deleteFirst _ [] = [] 
deleteFirst a (b:bc) | a == b    = bc 
                     | otherwise = b : deleteFirst a bc
           
findFirst _ [] = [] 
findFirst a (b:bc) | a == b    = bc 
                     | otherwise = b : findFirst a bc
                     
replaceNth n y xs | n > 0 = take (n-1) xs ++ y ++ drop n xs