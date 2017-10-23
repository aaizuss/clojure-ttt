<?php
    $host = "host=localhost";
    $port = "port=5432";
    $dbname = "dbname=ttt_experiment";
    $empty_board = "_________";

    $db = pg_connect("$host $port $dbname");

    if(!$db) {
      exit("There was an error establishing database connection\n");
    }

   $json = json_decode(file_get_contents("php://input"), true);
   $board_state = trim($json['board_state']);
   if(!$board_state) {
     $board_state = $empty_board;
   }

    $query_moves = "select state, move, count(*) as count from (select * from game where state = '$board_state') as s, unnest(s.moves) as move group by 1,2 order by 1,3 desc";

    $result = pg_query($db, $query_moves);
    $data = array();
    $data = pg_fetch_all($result);
    echo json_encode($data);
?>
