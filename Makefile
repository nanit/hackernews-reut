dbs-up:
	docker-compose up -d redistest dbtest

test:
	docker-compose up -d
	cd app; lein test
	docker-compose down
