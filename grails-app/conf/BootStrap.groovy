import domain.auth.SecRole
import domain.auth.SecUser
import domain.auth.SecUserSecRole
import domain.*

class BootStrap {

  def springSecurityService
  def init = { servletContext ->
    // Creating users
    if (!SecUser.count()) {
      def userRole = new SecRole(authority: 'ROLE_USER').save(failOnError: true)
      def adminRole = SecRole.findByAuthority('ROLE_ADMIN') ?: new SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)
      def password = springSecurityService.encodePassword('123')

      def adminUser = SecUser.findByUsername('admin') ?: new SecUser(
              username: 'admin',
              surname: 'Bob',
              realname: 'Bob',
              email: 'admin@admin.com',
              password: springSecurityService.encodePassword('admin'),
              enabled: true).save(failOnError: true)

      if (!adminUser.authorities.contains(adminRole)) {
        SecUserSecRole.create adminUser, adminRole
      }

      def user = new SecUser(username: 'qwe', surname: 'Zohan', realname: 'Bob', email: 'dus@dusca.du', password: password, enabled: true).save(failOnError: true)
      def user2 = new SecUser(username: 'asd', surname: 'Bob', realname: 'Frog', email: 'dus@dus.su', password: password, enabled: true).save(failOnError: true)
      SecUserSecRole.create user, userRole, true
      SecUserSecRole.create user2, userRole, true


    //Creating Curencies
    def cur1 = new Currency(code: 'rur', name: 'curencies.rur').save(failOnError: true)
    def cur2 = new Currency(code: 'usd', name: 'curencies.usd').save(failOnError: true)
    def cur3 = new Currency(code: 'eur', name: 'curencies.eur').save(failOnError: true)

    //Creating categories
    def ctg1 = new CategoryBill(name: 'UleBank', isChecked: true, color: 'blue', user: user).save(failOnError: true)
    def ctg2 = new CategoryBill(name: 'BufBank', isChecked: true, color: 'magenta', user: user).save(failOnError: true)

    def ctg3 = new CategoryOp(name: 'Bo2l', isChecked: true, color: 'magenta', user: user).save(failOnError: true)
    def ctg4 = new CategoryOp(name: 'Pay2', isChecked: true, color: 'red', user: user).save(failOnError: true)

    //Creating Bills
    def bill1 = new Bill(name: 'Card1', currency: cur1, balance: 1000, category: ctg1, isChecked: true).save(failOnError: true)
    def bill2 = new Bill(name: 'Card2', currency: cur2, balance: 4040, category: ctg1, isChecked: true).save(failOnError: true)
    def bill3 = new Bill(name: 'iCard', currency: cur2, balance: 4020, category: ctg1,  isChecked: true).save(failOnError: true)
    def bill4 = new Bill(name: 'bCard', currency: cur3, balance: 7000, category: ctg1, isChecked: true).save(failOnError: true)
    def bill5 = new Bill(name: 'sCard', currency: cur2, balance: 9040, category: ctg2, isChecked: true).save(failOnError: true)
    def bill6 = new Bill(name: 'Dep1', currency: cur3, balance: 1040, category: ctg2,  isChecked: true).save(failOnError: true)

    //Creating operations
    def op1 = new Operation(name: 'Колбасы', user: user,category: ctg3,  type: 2,bill: bill1, startDate: new Date(111,6,7), endDate: new Date(111,6,10)).save(failOnError: true)
    def op2 = new Operation(name: 'Вертолёт',user: user,category: ctg3,  type: 1,bill: bill2, startDate: new Date(111,6,8), endDate: new Date(111,6,8)).save(failOnError: true)
    def op3 = new Operation(name: 'Car', user: user,category: ctg3,     type: 1,bill: bill3, startDate: new Date(111,6,11), endDate: new Date(111,6,12)).save(failOnError: true)
    def op4 = new Operation(name: 'bear', user: user,category: ctg3,     type: 2,bill: bill3, startDate: new Date(111,6,13), endDate: new Date(111,6,13)).save(failOnError: true)
    def op5 = new Operation(name: 'dog house',user: user,category: ctg3,  type: 1,bill: bill5, startDate: new Date(111,6,4), endDate: new Date(111,6,5)).save(failOnError: true)

    //Updating categories
    ctg1.addToBills(bill1).addToBills(bill2).addToBills(bill3).addToBills(bill4)
    ctg2.addToBills(bill5).addToBills(bill6)

    ctg3.addToOperations(op1).addToOperations(op2).addToOperations(op3)
    ctg4.addToOperations(op4).addToOperations(op5)


//    Updating users
    user.addToCategoriesB(ctg1).addToCategoriesB(ctg2)
    user.addToCategoriesO(ctg3).addToCategoriesO(ctg4)
    user.addToOperations(op1 ).addToOperations(op2 ).addToOperations(op3 ).addToOperations(op5 ).addToOperations(op5 )

    }
  }
  def destroy = {
  }
}
