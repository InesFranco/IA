path(Node, Node, [Node]).

path(FirstNode, LastNode, [LastNode|Path]) :- path(FirstNode, OneButLastNode, Path), 
                                                s(OneButLastNode, LastNode),
                                                \+member(LastNode, Path).

s(Node, SNode) :- connected(Node, SNode).

connected(a, b).
connected(a, c).
connected(c, f).
connected(c, g).
connected(b, d).
connected(b, e).

goal(e).

depth_first_iterative_deepening(Node, Solution) :- path(Node, Goal, Solution), 
                                                    goal(Goal).




/*****
      a
    /   \
    b    c
  /  \   /\
  d   e f  g
**/