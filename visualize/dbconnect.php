<?php
    $host = "host=localhost";
    $port = "port=5432";
    $dbname = "dbname=ttt_experiment";

    $db = pg_connect("$host $port $dbname");

    if(!$db) {
      exit("There was an error establishing database connection\n");
    }

   $empty_board = '_________';

    $query_moves = "select state, move, count(*) as count from (select * from game where state = '$empty_board') as s, unnest(s.moves) as move group by 1,2 order by 1,3 desc";

    $result = pg_query($db, $query_moves);
    $data = array();
    $data = pg_fetch_all($result);
    echo json_encode($data);
?>
