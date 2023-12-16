import SwiftUI

struct LoginView: View {
    @Binding var isAuthenticated: Bool

    @State private var username: String = ""
    @State private var password: String = ""
    @State private var errorMessage: String?

    var body: some View {
        ZStack {
            LinearGradient(gradient: Gradient(colors: [Color.gray, Color.white]), startPoint: .top, endPoint: .bottom)
                .edgesIgnoringSafeArea(.all)

            VStack(spacing: 20) {
                Text("Добро пожаловать")
                    .font(.largeTitle)
                    .bold()

                TextField("Имя пользователя", text: $username)
                    .autocapitalization(.none)
                    .padding()
                    .background(Color.white)
                    .cornerRadius(5.0)
                    .shadow(radius: 10)
                    .padding([.leading, .trailing], 24)

                SecureField("Пароль", text: $password)
                    .padding()
                    .background(Color.white)
                    .cornerRadius(5.0)
                    .shadow(radius: 10)
                    .padding([.leading, .trailing], 24)

                if let errorMessage = errorMessage {
                    Text(errorMessage)
                        .foregroundColor(.red)
                }

                Button(action: {
                    AuthNetworkManager.shared.login(username: username, password: password) { success, error in
                        if success {
                            isAuthenticated = true
                        } else {
                            self.errorMessage = error ?? "Неизвестная ошибка"
                        }
                    }
                }) {
                    Text("Войти")
                        .font(.headline)
                        .foregroundColor(.white)
                        .padding()
                        .frame(width: 220, height: 60)
                        .background(Color.blue)
                        .cornerRadius(15.0)
                }
            }
            .padding()
        }
    }
}
