cd ../
docker-compose up -d
sleep .5
PGPASSWORD=TwwlZL9j10wyziG3 psql -h localhost -p 6002 -U qh47Qsmu19JJRuMq -d job_swipe -f db/initial_db.sql