// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table users {
  id integer [primary key]
  username varchar
  password varchar
  mobile varchar
  email varchar
  token varchar
  tokenDate date
  rating int
  created_at timestamp
}

Table contractors {
  id integer [primary key]
  username varchar
  password varchar
  mobile varchar
  email varchar
  token varchar
  tokenDate date
  rating int
  created_at timestamp
}
Table orders {
  id integer [primary key]
  parrentId integer 
  userId id
  routesId id
  stateId id
  contractorId int

  numberPassager int
  numberBags int
  note varchar
  role varchar
  startOn datetime
  created_at timestamp
}

Ref: orders.parrentId > orders.id // many-to-one

Table ordersState {
  id integer [primary key]
  name varchar
}

Table geolocations{
  id integer [primary key]
  username varchar
  latitude  number
  longitude number
  reliability int
  created_at timestamp
}

Table routes{
  id integer [primary key]
  routesId integer 
  geolocationId int
  order int
  created_at timestamp
}

/*todo*/
 Table todo.roles{
  principalid varchar /*NOTNULL*/
	role  varchar 
	rolegroup  varchar 

 }
	

Ref: routes.geolocationId > geolocations.id // many-to-one
Ref: orders.routesId > routes.routesId // many-to-one
Ref: orders.stateId > ordersState.id // many-to-one
Ref: orders.userId > users.id // many-to-one
Ref: orders.contractorId > contractors.id // many-to-one
