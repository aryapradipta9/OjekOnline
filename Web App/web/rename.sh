list=$(find -name '*.html' -type 'f')

for file in $list
do
	filename=$(echo $file | grep -o -P '.+(?=\.html)')
	mv "$filename".html "$filename".jsp
done