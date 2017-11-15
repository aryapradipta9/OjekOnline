<?php
	class SQLConnection {
		const username = "root";
		const password = "";
		const host = "127.0.0.1";
		const db_name = "db_ngeeeng";

		private $mysqli;

        /**
         * SQLSession constructor.
         * @param $tbl_name
         */
        public function __construct(){
			$this->mysqli = new mysqli(SQLConnection::host, SQLConnection::username, SQLConnection::password, SQLConnection::db_name);
		}

		public function __destruct(){
            $this->mysqli->close();
        }

        /**
         * Menjalankan query SQL dan mengembalikan mysqli_result.
         * @param $query String query SQL
         * @return bool|mysqli_result
         */
        public function runQuery($query){
			return $this->mysqli->query($query);
		}
	}
?>