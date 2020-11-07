echo "START!!!!"
cd /webchange/
echo " lein run migratus migrate "
lein migratus migrate
echo " lein run init-secondary "
lein run init-secondary 1 "demo@example.com" "123"
lein run dev