module Root_Simplified where

adjacent :: [Integer]
adjacent = []

levels :: [Integer]
levels = []

broadcast :: Bool
broadcast = False

new_node :: [Integer] -> [Integer]
new_node x = adjacent ++ x

fail :: Integer -> [Integer]
fail x = deleteFirst x adjacent 

-- this is a helper function for fail
deleteFirst _ [] = [] 
deleteFirst a (b:bc) | a == b    = bc 
                     | otherwise = b : deleteFirst a bc