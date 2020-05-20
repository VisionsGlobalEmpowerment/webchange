# School synchronization
School synchronization consist of two different process:
 
- First school instance initialization.
This is used only for first initialization and will usually be executed during equipment set up.  
- School synchronization.
This is used during normal education process, to send your students progress to our server.
At the same you will get update of your courses and assets.

## School initialization

First of all you should set up variables in 
```
bin/school-setup/config.edn
```
and in 
```
bin/school-setup/config.sh
```
Then you have to run:
```
bin/school-setup/install.sh
```
Then run this will load all data from server to local database
```
/srv/www/webchange/run load-secondary-school
```
This will set up database for you. Now you should be able to login, and create class, student and start education.

## School synchronization
School sycronization is very easy. You need to go:
```
http://you.site.url/dashboard/schools
```
Then select refresh button near school you wish to synchronize. Then confirm this action in dialog box.
When this process will be done, this dialog will be closed. 

## Course data download 
This should be used for development purposes. You should specify schoold id like in example bellow.
```
lein run download-course-data school-id
```
This will update all course data and download all assets.